<template>
    <h2 v-if="!isConnected()">No connection</h2>
    <h2 v-else-if="!isLoggedIn()">Log in to play the daily challenge</h2>
    <h2 v-else>You have completed the daily challenge come back tommorow</h2>
</template>

<script lang="ts">
import { Vue } from "vue-class-component";
import { useStore } from "vuex";
export default class NoDailyChallenge extends Vue {

    store: any = null;

    created() {
        this.store = useStore();
    }

    isConnected = () => {
        return this.store.state.onlineStatus;
    }

    isLoggedIn = () => {
        return this.store.state.auth.status.loggedIn;
    }
}
</script>