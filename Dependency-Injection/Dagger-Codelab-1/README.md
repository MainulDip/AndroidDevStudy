### Project Overview:
Link: https://github.com/android/codelab-android-dagger

* User Registration
- main.MainActivity will navigate the non-registered user to EnterDetailsFragment. then onCreateView will set up liveData observer and collect user's data
- After Adding Credentials, Next Button press will Trigger EnterDetailsViewModel's validate fn
- On validation success, fields will be stored, and as the property is a
- observed live data, if it changes, it will trigger the observe callback declared
- inside onCreateView of this (EnterDetailsFragment) class, which will again trigger
- RegistrationViewModel's updateUserData fn and trigger
- the RegistrationActivity's onDetailsEntered fn, from there, will call
- TermsAndCondition Fragment, if accepted, will call again the RegistrationActivity's
- onTermsAndConditionsAccepted() fn, which will call registrationViewModel.registerUser(),
- and finally it will call UserManager's register fn, from there the SharedPreference Store
- will be populated, and will instantiate UserDataRepository with current UserManager Object

### Some Brush-Up:

