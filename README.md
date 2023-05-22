# AureliaStorm
This plugin brings support for [Aurelia](http://aurelia.io) framework to the [IntelliJ platform](https://www.jetbrains.com/products.html?fromMenu#lang=js&type=ide).

Features:

* Code insight for specific Aurelia attributes (such as `*.bind` or `*.trigger`)
* Syntax highlighting in `${}` interpolations
* Controller properties completion and navigation
* New project generation via aurelia-cli
* Supports Aurelia 1 and 2

Either `aurelia` (v2), or `aurelia-cli` (v1) must be present in the project npm dependencies

## Contributing
### Prerequisites
* JDK version 17 or later suggested

### Running the plugin
In order to test your plugin use the [runIde](https://plugins.jetbrains.com/docs/intellij/configuring-plugin-project.html#run-ide-task) Gradle. This will launch an instance of intellij with the plugin loaded.
