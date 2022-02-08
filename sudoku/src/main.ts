import { createApp } from "vue";
import App from "./App.vue";
import "./registerServiceWorker";
import router from "./router";
import store from "./store";
import {initializeFirebase} from './push-notification';
import {askForPermissionToReceiveNotifications} from './push-notification'

createApp(App).use(store).use(router).mount("#app");

initializeFirebase();
askForPermissionToReceiveNotifications();