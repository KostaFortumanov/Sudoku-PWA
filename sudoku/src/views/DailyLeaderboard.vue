<template>
  <div class="leaderboard">
    <div v-if="connected">
      <div v-if="loading">
        <p>Loading...</p>
      </div>
      <div v-else>
        <table>
          <tr v-for="time in onlineTimes.slice(0, 10)" :key="time">
            <td>{{ time.order }}.{{ time.name }}</td>
            <td>{{ formatTime(time.time) }}</td>
          </tr>
          <tr v-if="onlineTimes.length > 10">
            <td>...</td>
          </tr>
          <tr v-if="onlineTimes.length > 10">
            <td>{{ onlineTimes[10].order }}.{{ onlineTimes[10].name }}</td>
            <td>{{ formatTime(onlineTimes[10].time) }}</td>
          </tr>
        </table>
      </div>
    </div>
    <div v-else>
      <p>No connection</p>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue } from "vue-class-component";
import LeaderboardService from "../services/leaderboard-service";
import { LeaderboardTime } from "../models/LeaderboardTime";
import { useStore } from "vuex";
export default class DailyLeaderboard extends Vue {
  onlineTimes: LeaderboardTime[] = [];
  loading = false;
  connected = false;
  store: any = null;

  created() {
    this.store = useStore();
    this.connected = this.store.state.onlineStatus;
  }

  mounted() {
    this.getTimes("daily");
  }

  getTimes(difficulty: string) {
    this.loading = true;
    LeaderboardService.getLeaderboard(difficulty).then((response) => {
      this.onlineTimes = response.data;
      this.loading = false;
    });
  }

  formatTime(time: number) {
    let sec: any = time % 60;
    let min: any = (time - sec) / 60;
    if (min < 10) min = "0" + min;
    if (sec < 10) sec = "0" + sec;
    return min + ":" + sec;
  }
}
</script>

<style lang="scss" scoped>
.leaderboard {
  font-weight: bold;
  color: #2c3e50;
}

table {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

tr td:first-child {
  text-align: left;
  min-width: 10em;
  max-width: 10em;
}
</style>