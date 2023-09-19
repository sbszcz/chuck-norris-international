package main

import (
	"context"
	"os"

	"dagger.io/dagger"
)

func main() {
	ctx := context.Background()
	client, err := dagger.Connect(ctx, dagger.WithLogOutput(os.Stdout), dagger.WithWorkdir(".."))
	if err != nil {
		panic(err)
	}
	defer client.Close()

	gradleContainer := client.
		Container().
		From("gradle:8.3")

	source := client.Host().Directory(".")
	gradleCache := client.CacheVolume("gradle")
	gradleContainer = gradleContainer.
		WithDirectory("/app", source, dagger.ContainerWithDirectoryOpts{
			Exclude: []string{
				"dagger-pipeline",
				".git",
				".github",
				".idea",
				".gradle",
				"gradle/wrapper",
				"gradlew",
				"gradlew.bat",
			},
		}).
		WithWorkdir("/app").
		WithMountedCache("/home/gradle/.gradle", gradleCache)

	container := gradleContainer.WithExec([]string{
		"gradle", "--no-daemon", "--console=plain", "componentTest",
	}, dagger.ContainerWithExecOpts{SkipEntrypoint: true})

	_, err = container.Stdout(ctx)
	if err != nil {
		panic(err)
	}
}
