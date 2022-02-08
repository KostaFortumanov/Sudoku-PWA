import { createStore } from "vuex";
import { auth } from "./auth-module";

const initialState = {onlineStatus: false};

const store = createStore({
  state: initialState,
  modules: {
    auth: auth,
  },
});

export default store;