import { createApp } from "vue";
import App from "./App.vue";
import "./registerServiceWorker";
import router from "./router";
import store from "./store";
import LeaderboardService from './services/leaderboard-service'

createApp(App)
  .use(store)
  .use(router)
  .mount("#app");

const handleConnection = () => {
  if (navigator.onLine) {
    isReachable(window.location.origin).then(function(online) {
      if (online) {
        console.log('online');
        backupTimes('easy');
        backupTimes('medium');
        backupTimes('hard');
      } else {
        console.log('no connectivity');
      }
    });
  } else {
    console.log('offline');
  }
}

const backupTimes = (difficulty: string) => {
  const t = window.localStorage.getItem(difficulty + "Time");
  let arr = [];
  if (t) {
    arr = JSON.parse(t);
  }

  for(let i=0; i<arr.length; i++) {
    if(!arr[i].isBackedUp) {
      const removed = arr.splice(i, 1);
      i--;
      window.localStorage.setItem(difficulty + "Time", JSON.stringify(arr));
      LeaderboardService.saveTime(difficulty, removed[0].time)
    }
  }
}

const isReachable = (url:string) => {
  return fetch(url, { method: 'HEAD', mode: 'no-cors' })
    .then(function(resp) {
      return resp && (resp.ok || resp.type === 'opaque');
    })
    .catch(function(err) {
      console.warn('[conn test failure]:', err);
    });
}

window.addEventListener('online', handleConnection);
window.addEventListener('offline', handleConnection);
