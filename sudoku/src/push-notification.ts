import firebase from 'firebase';
import NotificationService from './services/notification-service';
 
export const initializeFirebase = () => {
  firebase.initializeApp( {
    apiKey: "AIzaSyBaHGFoJ9acDvSLBHOzB8mYSdq5fPa_INA",
    authDomain: "sudoku-awd.firebaseapp.com",
    projectId: "sudoku-awd",
    storageBucket: "sudoku-awd.appspot.com",
    messagingSenderId: "735775189744",
    appId: "1:735775189744:web:26eb99b013a1bcc0db3035",
    measurementId: "G-2DPX858T80"
  });
}
 
export const askForPermissionToReceiveNotifications = async () => {
  try {
    const messaging = firebase.messaging();
    Notification.requestPermission().then(function (permission) {
      console.log('granted')
    });
    const token = await messaging.getToken();
    NotificationService.subscribe(token);
    window.localStorage.setItem('notificationToken', token);
    return token;
  } catch (error) {
    console.error(error);
  }
}