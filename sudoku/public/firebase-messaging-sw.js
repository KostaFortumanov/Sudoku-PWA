importScripts('https://www.gstatic.com/firebasejs/8.4.1/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/8.4.1/firebase-messaging.js');
firebase.initializeApp( {
    apiKey: "AIzaSyBaHGFoJ9acDvSLBHOzB8mYSdq5fPa_INA",
    authDomain: "sudoku-awd.firebaseapp.com",
    projectId: "sudoku-awd",
    storageBucket: "sudoku-awd.appspot.com",
    messagingSenderId: "735775189744",
    appId: "1:735775189744:web:26eb99b013a1bcc0db3035",
    measurementId: "G-2DPX858T80"
  });
const messaging = firebase.messaging();