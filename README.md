Unexpected behavior in Play validations
=======================================

Both `Form#fillAndValidate` and `Mapping#unbindAndValidate` unexpectedly don't apply global constraints.

The documentation seems to indicate that validations occur for `Mapping#unbindAndValidate`:

>    * Unbinds this field, i.e. transforms a concrete value to plain data, and applies validation.

and for `Form#fillAndValidate`:

>    * Fills this form with a existing value, and performs a validation.


Use case
--------

Unit testing validations by creating instances of a class and verifying that the validations successfully find the invalid data.  See test case for example.
