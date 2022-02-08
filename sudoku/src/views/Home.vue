<template>
  <div class="home">
    <div class="menu" v-if="!playing">
      <div class="menu-inner" :key="playing">
        <h2>Sudoku</h2>
        <button @click="startEasyGame">Easy</button>
        <button @click="startMediumGame">Medium</button>
        <button @click="startHardGame">Hard</button>
        <button @click="startSolver">Solver</button>
        <button @click="dailyChallenge">Daily challenge</button>
      </div>
    </div>
    <Game ref="game" v-if="playing" :currentDifficulty="difficulty" />
    <div class="menu" v-if="playing">
      <div class="menu-inner" :key="playing">
        <button
          v-if="solving"
          @click="
            startSolver();
            this.$refs.game.solve();
          "
        >
          Solve
        </button>
        <button @click="goBack">Back</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import Game from "@/components/Game.vue";

@Options({
  components: {
    Game,
  },
})
export default class Home extends Vue {
  playing = false;
  solving = false;
  difficulty!: string;

  startEasyGame() {
    this.difficulty = "easy";
    this.playing = true;
    this.solving = false;
  }

  startMediumGame() {
    this.difficulty = "medium";
    this.playing = true;
    this.solving = false;
  }

  startHardGame() {
    this.difficulty = "hard";
    this.playing = true;
    this.solving = false;
  }

  startSolver() {
    this.difficulty = "solve";
    this.playing = true;
    this.solving = true;
  }

  dailyChallenge() {
    this.difficulty = "daily";
    this.playing = true;
    this.solving = false;
  }

  goBack() {
    this.playing = false;
    this.solving = false;
  }
}
</script>

<style lang="scss">
.menu {
  position: absolute;
  z-index: 11;
  width: 100%;
  // height: 100%;
  display: flex;
  align-items: center;
  flex-direction: column;

  h2 {
    justify-content: center;
  }

  .menu-inner {
    display: flex;
    background: #ffffff;
    padding: 18px;
    flex-direction: column;

    button {
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
  }
}
</style>
