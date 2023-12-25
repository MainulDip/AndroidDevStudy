### Android Theming With Material 3 in Compose:

### App Overview:
`ReplyListContent.kt` is responsible for loading home email list (`ReplyEmailList`) or reply email list (`ReplyEmailDetail`).

Navigation Forward (card's click functionality) -> when clicked, it changes the state, which initiate a Recompose with the updated state. As state is hooked as a flow, it emits whenever any changes applied.


Navigation BackWord -> Same as forward, state changes initiate a recompose. 
