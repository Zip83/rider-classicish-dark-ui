plugins {
  id("java")
  id("org.jetbrains.intellij.platform") version "2.19.0"
}

group = "io.github.zip83"
version = providers.gradleProperty("pluginVersion").get()

repositories {
  mavenCentral()
  intellijPlatform {
    defaultRepositories()
    jetbrainsRuntime()
  }
}

dependencies {
  intellijPlatform {
    rider(providers.gradleProperty("platformVersion").get()) {
      useInstaller = false
    }
    jetbrainsRuntime()
  }

  testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(25))
  }
}

tasks {
  patchPluginXml {
    sinceBuild.set(providers.gradleProperty("pluginSinceBuild"))
    untilBuild.set(providers.gradleProperty("pluginUntilBuild"))
  }

  test {
    useJUnitPlatform()
  }
}
