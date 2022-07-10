# firebase-zombie-user-example

This repo only intents to showcase a problem related to the firebase android sdk authentication part and has no further usage.

Dependencies:

firebase authentication:
firebase bom:

## HowTo

This example application demonstrates a normal registration flow.

The app is started in a new session and FirebaseAuth will be initialized. Afterwards the user gets signed in anonymously, if there is no current registration.

Now the user can sign in using a "real" provider and it's account will be linked to the previous anonymous account.

So far, so simple.

To get the app in an invalid state, one has to do the steps above and now sign in anonymously OVER the current "real" account. Although this seems wrong, the sign in will succeed,
but the newly created anon account will

- return FALSE for 'isAnonymous'
- only contain 1 provider: firebase

Since our logic checks, that you can only log in / sign in, if `FirebaseUser.isAnonymous` is `TRUE`, we can never sign in again, without logging out the user first.



