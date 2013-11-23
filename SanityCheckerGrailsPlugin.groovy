class SanityCheckerGrailsPlugin {
    // the plugin version
    def version = "0.0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.7 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
        "src/docs/"
    ]

    // TODO Fill in these fields
    def title = "Sanity Checker Plugin" // Headline display name of the plugin
    def author = "Aaron Brown"
    def authorEmail = "brown.aaron.lloyd@gmail.com"
    def description = '''\
Utility for performing various Sanity Checks on arbitrary object data, \
providing a uniform set of responses for check failures.'''

    def documentation = "http://aaron-brown.github.io/grails-sanity-checker/docs/manual/index.html"
    def license = "APACHE"
    def issueManagement = [ system: "GITHUB", url: "https://github.com/aaron-brown/grails-sanity-checker/issues" ]
    def scm = [ url: "https://github.com/aaron-brown/grails-sanity-checker" ]
}
