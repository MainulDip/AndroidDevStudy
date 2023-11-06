### Project Overview:

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
* 

# Comparison between different branches
* Step 1 - `main` to `1_registration_main` ([Comparison](https://github.com/googlecodelabs/android-dagger/compare/main...1_registration_main))
* Step 2 - `1_registration_main` to `2_subcomponents` ([Comparison](https://github.com/googlecodelabs/android-dagger/compare/1_registration_main...2_subcomponents))
* Step 3 - `2_subcomponents` to `3_dagger_app` ([Comparison](https://github.com/googlecodelabs/android-dagger/compare/2_subcomponents...3_dagger_app))
* Step 4 - `3_dagger_app` to `solution` ([Comparison](https://github.com/googlecodelabs/android-dagger/compare/3_dagger_app...solution))
* [Full codelab comparison](https://github.com/googlecodelabs/android-dagger/compare/main...solution)

```
