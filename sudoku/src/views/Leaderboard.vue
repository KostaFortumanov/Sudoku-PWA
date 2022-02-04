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
        <span :class="{ active: easyTimes }" class="link" @click="getTimes('easy')">Easy</span>
        |
        <span :class="{ active: mediumTimes }" class="link" @click="getTimes('medium')"
          >Medium</span
        >
        |
        <span :class="{ active: hardTimes }" class="link" @click="getTimes('hard')">Hard</span>
      </div>
    </div>
    <div v-for="(time, index) in times" :key="time">
      <p><span class="number">{{ index + 1 }}.</span> <span>{{ formatTime(time) }}</span></p>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue } from "vue-class-component";
export default class Leaderboard extends Vue {
  times = [];
  myTimes = true;
  easyTimes = true;
  mediumTimes = false;
  hardTimes = false;

  mounted() {
    this.getTimes('easy');
  }

  changeType() {
    this.times = [];
    this.myTimes = !this.myTimes;
    this.getTimes('easy')
  }

  getTimes(difficulty: string) {
    this.easyTimes = difficulty == 'easy';
    this.mediumTimes = difficulty == 'medium';
    this.hardTimes = difficulty == 'hard';
    if (this.myTimes) {
      this.times = [];
      let time = window.localStorage.getItem(difficulty + "Time");
      if (time) {
        this.times = JSON.parse(time);
        this.times.sort((a, b) => a - b);
      }
    }
  }

  getMediumTimes() {
    this.easyTimes = false;
    this.mediumTimes = true;
    this.hardTimes = false;
    if (this.myTimes) {
      this.times = [];
      let time = window.localStorage.getItem("mediumTime");
      if (time) {
        this.times = JSON.parse(time);
        this.times.sort((a, b) => a - b);
      }
    }
  }

  getHardTimes() {
    this.easyTimes = false;
    this.mediumTimes = false;
    this.hardTimes = true;
    if (this.myTimes) {
      this.times = [];
      let time = window.localStorage.getItem("hardTime");
      if (time) {
        this.times = JSON.parse(time);
        this.times.sort((a, b) => a - b);
      }
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

<style lang="scss">
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

.number {
  margin-right: 100px;
}
</style>