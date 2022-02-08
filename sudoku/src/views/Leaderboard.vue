<template>
  <div class="leaderboard">
    <div id="nav">
      <div>
        <span :class="{ active: myTimes }" class="link" @click="changeType()"
          >My times</span
        >
        |
        <span :class="{ active: !myTimes }" class="link" @click="changeType()"
          >Online</span
        >
        <br /><br />
      </div>
      <div>
        <span
          :class="{ active: easyTimes }"
          class="link"
          @click="getTimes('easy')"
          >Easy</span
        >
        |
        <span
          :class="{ active: mediumTimes }"
          class="link"
          @click="getTimes('medium')"
          >Medium</span
        >
        |
        <span
          :class="{ active: hardTimes }"
          class="link"
          @click="getTimes('hard')"
          >Hard</span
        >
      </div>
    </div>
    <div v-if="myTimes">
      <div v-for="(time, index) in times" :key="time">
        <p>
          <span class="number">{{ index + 1 }}.</span>
          <span>{{ formatTime(time) }}</span>
        </p>
      </div>
    </div>
    <div v-if="!myTimes">
      <div v-if="isConnected()">
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
  </div>
</template>

<script lang="ts">
import { Vue } from "vue-class-component";
import LeaderboardService from "../services/leaderboard-service";
import { LeaderboardTime } from "../models/LeaderboardTime";
import { useStore } from "vuex";
export default class Leaderboard extends Vue {
  times = [];
  onlineTimes: LeaderboardTime[] = [];
  myTimes = true;
  easyTimes = true;
  mediumTimes = false;
  hardTimes = false;
  loading = false;
  connected = false;
  store: any = null;

  created() {
    this.store = useStore();
  }

  isConnected = () => {
    return this.store.state.onlineStatus;
  }

  mounted() {
    this.getTimes("easy");
  }

  changeType() {
    this.times = [];
    this.myTimes = !this.myTimes;
    this.getTimes("easy");
  }

  getTimes(difficulty: string) {
    this.easyTimes = difficulty == "easy";
    this.mediumTimes = difficulty == "medium";
    this.hardTimes = difficulty == "hard";
    if (this.myTimes) {
      this.times = [];
      let time = window.localStorage.getItem(difficulty + "Time");
      if (time) {
        this.times = JSON.parse(time).map((t: { time: number }) => t.time);
        this.times.sort((a, b) => a - b);
      }
    } else {
      this.loading = true;
      LeaderboardService.getLeaderboard(difficulty).then((response) => {
        this.onlineTimes = response.data;
        this.loading = false;
      });
    }
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

  .link {
    cursor: pointer;
  }

  .active {
    color: #1489ff;
  }
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

.number {
  margin-right: 100px;
}
</style>