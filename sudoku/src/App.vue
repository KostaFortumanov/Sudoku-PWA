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
export default class App extends Vue {
  store: any = null;
  username = "";

  created() {
    this.store = useStore();
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
