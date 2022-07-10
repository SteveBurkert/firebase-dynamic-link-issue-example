# firebase-zombie-user-example

Minimal example of an issue related to the 
firebase android sdk. 
This repo only intents to showcase the problem
and has no further usage.

Dependencies:

firebase authentication: 
firebase bom: 

## How to reproduce the problem

1. start the app 
2. sign in using test credentials
3. sign in anonymously _over_ the current account

Watch the logs while doing so. 
The app will sign in anonymously on first app start. 
The logs will reflect that.
After you signed in with a "real" (although test) account,
the app will display the status and variables given by the sdk.
It will also display the authentication provider, used by this account.

What to expect after signing in anonymously over the current account?

The sdk will return that the current user is not an anonymously signed in user,
although it is. The current provider will be 'firebase'.


