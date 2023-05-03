package mocks;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.Slf4jNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

public class BaseWireMock {

    private static final Logger log = LoggerFactory.getLogger(BaseWireMock.class);

    private final WireMockServer server;
    protected WireMock wireMock;

    protected String host;
    protected int port;


    public BaseWireMock(String host, int port) {

        this.host = host;
        this.port = port;

        log.debug("Start Wiremock Server");
        server = new WireMockServer(
                WireMockConfiguration.options()
                        .port(port)
                        .notifier(new Slf4jNotifier(true))
                        .usingFilesUnderDirectory("build/ct")
        );
        server.start();
        wireMock = new WireMock(server);
    }

    public void startIfNotRunning(){

        if (server.isRunning()) {
            log.debug("Attempt to start wiremock skipped. Wiremock server still running");
            return;
        }

        log.debug("Starting wiremock");
        server.start();
        try {
            waitForServerPortToBeOpen();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        server.stop();
    }

    public void reset() {
        server.resetAll();
    }

    private void waitForServerPortToBeOpen() throws InterruptedException {
        for (int i = 0; i < 500; i++) {
            try (SocketChannel server = SocketChannel.open()) {
                SocketAddress socketAddr = new InetSocketAddress("http://" + host, port);
                server.connect(socketAddr);

                if (server.isOpen()) {
                    break;
                }
            } catch (Exception e) {
                Thread.sleep(10);
            }
        }
    }

    protected void verifyNoRequestIssuedTo(RequestPatternBuilder requestPatternBuilder) {
        wireMock.verifyThat(0, requestPatternBuilder);
    }

    public void verifyNoCallHasBeenMade() {
        verifyNoRequestIssuedTo(new RequestPatternBuilder(RequestMethod.ANY, urlMatching("/.*")));
    }
}
