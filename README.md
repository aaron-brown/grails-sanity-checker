# Grails SanityChecker Plugin

Designed to be a simple, yet powerful, utility for intuitively interacting with
data and basic datatypes.

Sample use cases:

  * Check properties of `def` variables (or staticly type them...)
  * Check forms of input (i.e. read a file and verify certain conventional
    integrities were maintained.)
  * Check and respond appropriately to well-structured JSON data (for restful
    services)
  * Check and verify elements or data in a `List` or `Map`.
  * Check and Coerce normalized input of simple data types.
  * Check required parameters, and then `allowPassOnNull` to check optional
    parameters.

## Advantages

  * Uniform interface for catching check failures.
  * Uniform output.
  * Easy to extend or expand upon.
  * The Built-in expanded-on Checkers are more versitile and useful than the
    basic Checker.

## Disadvantages

I can't really think of any. Possibly misuse of the 
`StringCoerciveSanityChecker` if not careful.

# Usage

## `BasicSanityChecker`

The `BasicSanityChecker` is used for simple sanity checking:

    public void myMethod(Map options, String a, Date birthday, int… luckyNumbers) {
        BasicSanityChecker checker = new BasicSanityChecker().runChecks {
            check('a', a).isNotEmpty()

            check('birthday', myBirthday).isNotNull()

            luckyNumbers.eachWithIndex { index, number -> 
                check("luckyNumber[${index}]", number).isNotNull() 
            }

            allowPassOnNull()

            options.with { 
                check('favoriteColor', myFavoriteColor) isString() isNotEmpty()

                check('height', myHeight) isDouble()

                check('myEyesAreBlue', myEyesAreBlue) isBoolean() 
            } 
        }

        if (checker.hasFailures) { 
            // Handle the result. 
        } 
    }

## `StringCoerciveSanityChecker`

The `StringCoerciveSanityChecker` does type-checking, but also coerces
`String` objects that pass the check into the datatype checked for. If the
check fails, the same object is returned to prevent data loss:

    import me.sudofu.sanitycheck.SanityChecker
    import me.sudofu.sanitycheck.BasicSanityChecker
    import me.sudofu.sanitycheck.StringCoerciveSanityChecker as SCSanityChecker

    public void myMethod(Map options, String a, Date birthday, int… luckyNumbers) { 
      SanityChecker checker = new SCSanityChecker().runChecks { 
          check('a', a).isNotEmpty()

          check('birthday', myBirthday).isNotNull()

          luckyNumbers.eachWithIndex { index, number -> 
              luckyNumbers[index] = check("luckyNumber[${index}]", number).isNotNull() 
          }

          allowPassOnNull()

          options.with { check('favoriteColor', myFavoriteColor) isString() isNotEmpty()

          myHeight = check('height', myHeight) } }

          if (checker.hasFailures()) { 
              // Handle the result. 
          }

          checker = new BasicSanityChecker() 
              .check('myEyesAreBlue', options.myEyesAreBlue) 
              .isBoolean()

          if (checker.hasFailures()) { 
              // Handle the result. 
          } 
      }
