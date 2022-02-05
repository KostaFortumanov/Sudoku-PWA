<template>
  <div class="login">
    <form class="login-form" @submit.prevent="onLogin">
      <h2>Login</h2>
      <div class="input-group">
        <label for="username">Username</label
        ><input id="username" type="text" v-model="username" required />
      </div>
      <div class="input-group">
        <label for="password">Password</label
        ><input id="password" type="password" v-model="password" required />
      </div>
      <div class="error" v-if="error">Invalid username or password</div>
      <button class="button" type="submit">Login</button>
    </form>
  </div>
</template>

<script lang="ts">
import { Vue } from "vue-class-component";
import { useStore } from "vuex";
import router from "../router/index";
export default class Login extends Vue {
  username = "";
  password = "";
  error = false;
  store: any = null;

  mounted() {
    this.store = useStore();
    if (this.isLoggedIn()) {
      router.push("/");
    }
  }

  onLogin = () => {
    console.log(this.store);
    this.store
      .dispatch("auth/login", {
        username: this.username,
        password: this.password,
      })
      .then(
        () => {
          router.push("/");
        },
        () => {
          this.error = true;
        }
      );
  };

  isLoggedIn = () => {
    return this.store.state.auth.status.loggedIn;
  };
}
</script>


<style lang="scss">
.login {
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-form {
  display: flex;
  flex-direction: column;
  background: #fff;
  border-radius: 0.25rem;
  padding: 1rem;
  //   box-shadow: 0 0 2rem rgba(0,0,0,0.5);
}

.input-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 1rem;
}

.error {
  color: #f00;
  margin-bottom: 1rem;
  text-align: center;
}

.button {
  color: #ffffff;
  font-size: 20px;
  border: solid 1px #1489ff;
  background: #1489ff;
  cursor: pointer;
  border-radius: 0.5rem;
  padding: 1rem 2rem;
  margin-bottom: 1em;

  &:hover {
    background: #ffffff;
    color: #1489ff;
  }
}
</style>