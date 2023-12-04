### Overview:
Docs
1. https://developer.android.com/guide/topics/resources/animation-resource#Property
2. https://developer.android.com/develop/ui/views/animations/overview

### Animation Resource File:
An animation resource can define one of two types of animations:

1. `Property` animation
Creates an animation by modifying an object's property values over a set period with an Animator.


2. `View` animation
There are two types of animations that you can do with the view animation framework:

- `Tween` animation: creates an animation by performing a series of transformations on a single image with an Animation.
    
- `Frame` animation: creates an animation by showing a sequence of images in order with an AnimationDrawable.

### Property Animation:
An animation defined in XML that modifies properties of the target object, such as background color or alpha value, over a set amount of time.
```xml
<set android:ordering="sequentially">
    <set>
        <objectAnimator
            android:propertyName="x"
            android:duration="500"
            android:valueTo="400"
            android:valueType="intType"/>
        <objectAnimator
            android:propertyName="y"
            android:duration="500"
            android:valueTo="300"
            android:valueType="intType"/>
    </set>
    <objectAnimator
        android:propertyName="alpha"
        android:duration="500"
        android:valueTo="1f"/>
</set>
```
```kotlin
// Using Animation
val set: AnimatorSet = AnimatorInflater.loadAnimator(myContext, R.animator.property_animator)
    .apply {
        setTarget(myObject)
        start()
    }
```

### View Animation:
The view animation framework supports both tween and frame-by-frame animations
```xml
<set xmlns:android="http://schemas.android.com/apk/res/android"
    android:shareInterpolator="false">
    <scale
        android:interpolator="@android:anim/accelerate_decelerate_interpolator"
        android:fromXScale="1.0"
        android:toXScale="1.4"
        android:fromYScale="1.0"
        android:toYScale="0.6"
        android:pivotX="50%"
        android:pivotY="50%"
        android:fillAfter="false"
        android:duration="700" />
    <set
        android:interpolator="@android:anim/accelerate_interpolator"
        android:startOffset="700">
        <scale
            android:fromXScale="1.4"
            android:toXScale="0.0"
            android:fromYScale="0.6"
            android:toYScale="0.0"
            android:pivotX="50%"
            android:pivotY="50%"
            android:duration="400" />
        <rotate
            android:fromDegrees="0"
            android:toDegrees="-45"
            android:toYScale="0.0"
            android:pivotX="50%"
            android:pivotY="50%"
            android:duration="400" />
    </set>
</set>
```
```kotlin
// Use animation
val image: ImageView = findViewById(R.id.image)
val hyperspaceJump: Animation = AnimationUtils.loadAnimation(this, R.anim.hyperspace_jump)
image.startAnimation(hyperspaceJump)
```

### Animation With NavController:
NavController can use animation defined in
- Navigation Graph's `action`, which will automatically picked
- Creating a NavOptions object using `navOptions{anime{...}}` (NavOptionsBuilder) and passing it by `findNavController().navigate(action, navOptions)`

Note : if animation is in xml's action, no need for navOption only for animations.
```xml
<fragment
    android:id="@+id/home_dest"
    ...>

    <action
        android:id="@+id/next_action"
        app:destination="@id/flow_step_one_dest"
        app:enterAnim="@anim/slide_in_right"
        app:exitAnim="@anim/slide_out_left"
        app:popEnterAnim="@anim/slide_in_left"
        app:popExitAnim="@anim/slide_out_right" />
</fragment>
```

```kotlin
val options = navOptions {
    anim {
        enter = R.anim.slide_in_right
        exit = R.anim.slide_out_left
        popEnter = R.anim.slide_in_left
        popExit = R.anim.slide_out_right
    }
}

// both can be used
// val action = HomeFragmentDirections.nextAction()
// findNavController().navigate(action, options)

findNavController().navigate(resId = R.id.flow_step_one_dest, args = null, navOptions = options)
```