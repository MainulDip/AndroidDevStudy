## Android With Java Overview:
This is a personalized overview of the core android development concepts and terms with some practical walk through docs

### :cat: [Android View/Layout](./android-view-cheat.md) < Click to view

### Adding Click Listener :
> Button extends TextView and TextView extends View, so in root button is a View :

```java
// add click listener adding a onClick function from the activity.xml to activity.java
// Or set click listener programatically by passing View.OnClickListener interface
Button btnClickMe = findViewById(R.id.btnClickMe);
btnClickMe.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        System.out.println("Hello Console");
        Toast.makeText(MainActivity.this, "Hello Toast", Toast.LENGTH_SHORT).show();
    }
});

// Or by implementing View.OnClickListener interface to the activity and overriding the method and setting the click listener on that method by

btnClickMe.setOnClickListener(this);

// and

@Override
public void onClick(View view) {
    switch (view.getId()){
        case R.id.btnClickMe:
            System.out.println("Priting from interface override");
            break;
        default:
            System.out.println("Printing from default case");
            break;
    }
}
```
> Every UI Element is a view (extending from View) like TextView, EditText ect.

> Different Listeners: type element.setListener and wait/look for IDE suggessions. Like setLongClickListener and ..dragListener etc.

### RecyclerView:
### :cat: [RecyclerView Implementation](./AndroidRecyclerView/README.md) < Click to view
### :cat: [RecyclerView Implementation](./BookManagerAndroid//README.md) < Click to view

### AlertDialog: AlertDialog.Builder
```java
AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
alertDialog.setMessage("Are you sure?");
alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
@Override
public void onClick(DialogInterface dialogInterface, int i) {
    Utils.deleteBook(book, store);
    notifyItemChanged(getAdapterPosition());
}
});
alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
@Override
public void onClick(DialogInterface dialogInterface, int i) {
    Toast.makeText(context, "Deleting Canceled", Toast.LENGTH_SHORT).show();
}
});

alertDialog.create().show();
```


### Shared Preferences:
> Key value paired type of storate

```java
private static ArrayList<Book> allBooks;
private static SharedPreferences spManagerBook;
private static SharedPreferences.Editor spEditor;
private static Type collectionType = new TypeToken<ArrayList<Book>>(){}.getType();
private static Gson gson = new Gson();
private Context context;

spManagerBook = context.getSharedPreferences(MANAGER_BOOK, Context.MODE_PRIVATE);
spEditor = spManagerBook.edit();

ArrayList<Book> data = new ArrayList<>(Arrays.asList(
        new Book(1, "FistBook", "Authorfirst", "https://www.snk-corp.co.jp/us/games/kof-xv/characters/img/character_kula.png", "small description", "This is long Description", 100),
        new Book(2, "SecondBook", "AuthorSecond", "https://static.wikia.nocookie.net/snk/images/4/47/Kof_xv_iori_render.png", "small description", "This is long Description", 100)
));

spEditor.putString(ALL_BOOKS, gson.toJson(data));
spEditor.commit();

allBooks = gson.fromJson(spManagerBook.getString(ALL_BOOKS, ALL_BOOKS), collectionType);
```

### Checking null case
```java
/**
* as getString will return default string if nothing found, we can check that for null/errors case
*/

if(!spManagerBook.getString(ALL_BOOKS, ALL_BOOKS).equals(ALL_BOOKS)){
    allBooks = gson.fromJson(spManagerBook.getString(ALL_BOOKS, ALL_BOOKS), collectionType);
} else {
    initData();
}
```

### Docs: https://github.com/google/gson/blob/master/UserGuide.md

### Personal Implementation :cat: [Shared Preferences Implementation](./StorageAnimationWebview/app/src/main/java/com/websolverpro/bookmanagerandroid/Utils.java) < Click to view