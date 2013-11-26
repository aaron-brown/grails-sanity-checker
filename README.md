# Grails SanityChecker Plugin

Designed to be a simple, yet powerful, utility for intuitively interacting with
data and basic datatypes.

Sample use cases:

  * Check properties of `def` variables (or staticly type them...)
  * Check forms of input (i.e. read a file and verify certain conventional
    integrities were maintained.)
  * Check and respond appropriately to well-structured JSON data (for restful
    services)<sup>\*</sup>
  * Check and verify elements or data in a `List` or `Map`.

\*: The goal of later versions is to provide sophisticated support for this
use case out-of-the-box.

## Advantages

  * Uniform interface for catching check failures
  * Uniform error output.
  * Easy to extend or expand upon.
  * The Built-in expanded-on Checkers are more versitile and useful than the
    basic Checker.

## Disadvantages / Trade-offs

  * A little verbose (not for someone who wants to do this themselves).
  * Cumbersome if validation of more than one item at a time is desired (see
    sample).
  * Over-engineered solutions to simple problems!

# Usage

# SanityChecker

The `SanityChecker` itself is fairly strict, and its uses are for very simple
checks. Here is an example for checking the sanity of a function with
named-parameters that would otherwise break awesome code:

    import me.sudofu.sanitycheck.SanityChecker
    import me.sudofu.sanitycheck.SanityCheckException

    def awesomeFunction(Map optionalParams, int aNumber, UUID identifier) throws IllegalArgumentException {
        List illegalArguments = []

        try {
            SanityChecker.checkFor('identifier', identifier)  {
                isNotNull()
            }
        } catch (Exception e) {
            illegalArguments << e.message
        }

        try {
            SanityChecker.checkFor('gender', optionalParams.gender, "optional parameter", true) {
                exactClassMatch(GenderEnum)
            }
        } catch (SanityCheckException e) {
            illegalArguments << e.message
        }

        try {
          SanityChecker.checkFor('height', optionalparams.height, "optional parameter", true) {
              isDouble()
          }
        } catch (SanityCheckException e) {
          illegalArguments << e.message
        }

        if (! illegalArguments.isEmpty()) {
            throw new IllegalArgumentException("The following information was rejected from the request: ${illegalArguments.join(';')}")
        }

        // Awesome code.
    }

On its own, the `SanityChecker` is probably not the best thing to use in every
function. It is best used for entry-points in long operations, or where data
is entered by a user to try and mitigate those problems.

If anything, it is a decent springboard for extension to put your own custom
paradigm into place. While it does not run a gambit, there is no saying it can
be used in an object specifically dedidcated to running a suite of checks.

# CoerciveSanityChecker

The `CoerciveSanityChecker` is probably the more useful of the (current)
implement Checkers. It has the same limitations in terms of verbosity, however
it has the addaded advantage of using **Groovy's** built-ins to coerce `String`
entities into the appropriate check.

Not only that, but should to coercion succeed, the result of the check passes
along the coerced entity, providing implicit conversion. This is useful for
inputs or methods that are designed to have `Strings` serve as a layer of
normalization, such as in a JAXRS Resource:

    // Setting them as different names to cut down on verbosity...
    import me.sudofu.sanitycheck.SanityChecker as Checker
    import me.sudofu.sanitycheck.CoerciveSanityChecker as FlexChecker
    import me.sudofu.sanitycheck.SanityCheckException

    import javax.ws.rs.*
    import javax.ws.rs.core.*

    import groovy.json.JsonSlurper

    @Path('/v1/myApi')
    @Consumes(['application/json'])
    @Produces(['application/json'])
    class MyApiResource {

        // Inject a Service.
        RegistrationService registrationService
        
        @PUT
        @Path('register')
        Response register(String json) {
            // Note: Plans are for a Checker related to JSON out-of-the-box.
            
            Map userInput
            
            // Parse JSON.

            List badData = []

            try {
                FlexChecker.checkFor('username', userInput.username) {
                    isString()
                }
            } catch (SanityCheckException e) {
                badData << e.message
            }
        }
    }
