In this project we built a chat application using android studio and a non-sql, cloud database called Firebase database.
Firebase is a backend server used to store data. We used the real-time database along with Firebase's newest database, cloud Firestore, which supports faster queries, faster storing structure and other additional features, so we benefit from both. 
The application first asks for your number to send you the verification code and verifies that you’re not a robot. Once it’s sent, you get redirected to a form where you’re asked to set up your profile. By pressing the save button, your username and status (online or not) is sent to the database with a unique ID, and your profile picture is sent to the storage.
 After authenticating your profile, you get redirected to the main screen where you can see chat, status and calls fragment. In the chats fragment you can see all your chats along with the profile picture, name, status of the contact, and you’re now ready to send and receive messages just by pressing on the desired chat. We used RecycleView to contain the views corresponding to our data, where each individual element in the list is defined by a view holder object. 
RecyclerView is also used in the MessageAdapter class which controls the sender messages being on the right side of the screen, and the receiver on the left.