<template>
  <div id="nav">
    <router-link to="/">Home</router-link> |
    <router-link to="/leaderboard">Leaderboard</router-link> |
    <router-link to="/daily">Daily challenge</router-link> <br />
    <br />
    <div v-if="!isLoggedIn()">
      <router-link to="/login">Login</router-link> |
      <router-link to="/register">Register</router-link>
    </div>
    <div id="loggedIn" v-if="isLoggedIn()">
      {{ getUsername() }} | <span class="link" @click="onLogout">Logout</span>
    </div>
  </div>
  <router-view />
</template>

<script lang="ts">
import { Vue } from "vue-class-component";
import { useStore } from "vuex";
import LeaderboardService from "./services/leaderboard-service";
export default class App extends Vue {
  store: any = null;
  username = "";

  created() {
    this.store = useStore();
    this.handleConnection();
    window.addEventListener("online", this.handleConnection);
    window.addEventListener("offline", this.handleConnection);
  }

  getUsername = () => {
    if (this.isLoggedIn()) {
      this.username = this.store.state.auth.user.username;
    }

    return this.username;
  };

  onLogout = () => {
    this.store.dispatch("auth/logout");
  };

  isLoggedIn = () => {
    return this.store.state.auth.status.loggedIn;
  };

  handleConnection = () => {
    if (navigator.onLine) {
      this.isReachable(window.location.origin).then((online) => {
        if (online) {
          this.store.state.onlineStatus = true;
          console.log("online");
          this.backupTimes("easy");
          this.backupTimes("medium");
          this.backupTimes("hard");
        } else {
          console.log("no connectivity");
          this.store.state.onlineStatus = false;
        }
      });
    } else {
      console.log("offline");
      this.store.state.onlineStatus = false;
    }
  };

  backupTimes = (difficulty: string) => {
    const t = window.localStorage.getItem(difficulty + "Time");
    let arr = [];
    if (t) {
      arr = JSON.parse(t);
    }

    for (let i = 0; i < arr.length; i++) {
      if (!arr[i].isBackedUp) {
        const removed = arr.splice(i, 1);
        i--;
        window.localStorage.setItem(difficulty + "Time", JSON.stringify(arr));
        LeaderboardService.saveTime(difficulty, removed[0].time);
      }
    }
  };

  isReachable = (url: string) => {
    return fetch(url, { method: "HEAD", mode: "no-cors" })
      .then(function (resp) {
        return resp && (resp.ok || resp.type === "opaque");
      })
      .catch(function (err) {
        console.warn("[conn test failure]:", err);
      });
  };
}
</script>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;

  a {
    font-weight: bold;
    color: #2c3e50;

    &.router-link-exact-active {
      color: #1489ff;
    }
  }
}

#loggedIn {
  font-weight: bold;
  color: #2c3e50;

  .link {
    cursor: pointer;
  }
}
</style>
