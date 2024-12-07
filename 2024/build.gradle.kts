plugins {
  scala
  application
}

dependencies {
  implementation("org.scala-lang:scala3-library_3:latest.release")

  implementation("org.typelevel:cats-core_3:latest.release")

//  implementation("org.processing:core:latest.release")
  implementation("com.google.guava:guava:latest.release")

  implementation("com.googlecode.lanterna:lanterna:latest.release")

  testImplementation("org.scalatest:scalatest_3:latest.release")
  testImplementation("org.scalacheck:scalacheck_3:latest.release")

  testRuntimeOnly("org.junit.platform:junit-platform-engine:latest.release")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher:latest.release")
  testRuntimeOnly("org.scalatestplus:junit-5-11_3:latest.release")

  testImplementation("org.openjdk.jmh:jmh-core:latest.release")
  testImplementation("org.openjdk.jmh:jmh-generator-bytecode:latest.release")

  components.all {
    val lcVersion = id.version.lowercase()
    if (
      lcVersion.contains("alpha")
      || lcVersion.contains("-b")
      || lcVersion.contains("beta")
      || lcVersion.contains("cr")
      || lcVersion.contains("m")
      || lcVersion.contains("rc")
      || lcVersion.contains("snap")
      || lcVersion.startsWith("200")
    ) {
      // Tell Gradle to not treat pre-releases as 'release'
      status = "integration"
    }
  }
}

dependencyLocking {
  lockAllConfigurations()
}

sourceSets {
  main {
    scala {
      srcDir("src")
    }
  }
}


java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
  }
}

application {
  mainClass.set("Foo")
}

tasks {
  compileJava {
    options.encoding = "UTF-8"
  }
  test {
    jvmArgs("-Dfile.encoding=UTF-8")
    useJUnitPlatform {
      includeEngines("scalatest")
      testLogging {
        events("passed", "skipped", "failed", "standard_error")
      }
    }
  }
}
