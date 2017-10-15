# X-FIT

## Provides built-in solutions for following indigenous android problems:

* Inter-activity communication (flags, intents, etc.)
* Saving state through configuration change
* Networking (managing caches, preventing leaks)
  * All kicked-off network requests will be delivered to new controllers upon orientation change
  * All kicked-off network requests will be restarted upon activity recreation

# How to use
## Instalation
## Setup
### Controllers
1. Define your xml layout. Note that your controller variable must be named **controller**. 
 ```XML
 <layout xmlns:bind="http://schemas.android.com/apk/res-auto">

     <data>

         <variable
             name="controller"
             type="com.example.MyController"/>

         <import type="com.thecvvm.utils.LayoutManagers"/>
     </data>

     <com.thecvvm.utils.HackyRecyclerView
         android:layout_width="match_parent"
         android:layout_height="match_parent"
         bind:adapter="@{controller.adapter}"
         bind:layoutManager="@{LayoutManagers.LINEAR_VERTICAL}"/>

 </layout>
 ```

2. Now implement the Controller that will manage your view.
 ```JAVA
 public class RepositoriesController extends Controller<LayoutExampleBinding> {
    
    @Bindable BaseAdapter<BaseVM> adapter;

     //...
 }
 ```

3. Now you can simple change controllers my calling router methods. Your activity must extend ControllerActivity and call setControllerContainer in onCreate method.
 ```JAVA
 public class MainActivity extends ControllerActivity {

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
         setControllerContainer(R.id.container);
         show(new RepositoriesController());
     }
 }
 ```

### Networking
1. Register your service interface through `Request` class. Best place to do this is Application's onCreate method. Note that your service must be an interface to be correctly proxied.
 ```JAVA
 Requests.registerService(api);
 ```
 
2. Use your service with the requests lib within a controller.
 ```JAVA
Request.with(this, Api.class)
        .create(api -> api.getUser("bolein"))
        .execute(user -> {
            App.getBus().post(new TestEvent(user.login));
            back();
        });
 ```

the end
