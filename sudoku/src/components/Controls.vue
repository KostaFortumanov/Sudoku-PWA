<template>
  <div class="controls">
    <div class="numpad-row" v-for="(row, index) in numpad" :key="index">
      <template v-for="num in row" :key="num">
        <button
          class="numpad-button"
          :value="num"
          @click="$emit('click', ['numpad', num])"
        >
          {{ num }}
        </button>
      </template>
      <button class="numpad-button" @click="$emit('click', ['delete'])">
        X
      </button>
    </div>
  </div>
</template>

<script lang="ts">
import { Options, Vue } from "vue-class-component";
import Sudoku from "../services/sudoku";

@Options({
  emits: ["click"],
  props: {
    notesMode: Boolean,
  },
})
export default class Controls extends Vue {
  numpad = [["1", "2", "3", "4", "5", "6", "7", "8", "9"]];
}
</script>

<style lang="scss">
.controls {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 10px;
}

.controls button {
  width: 50%;
  padding: 12px;
  color: #222222;
  font-size: 16px;
  border: 2px solid #222222;
  background-color: #ffffff;
  cursor: pointer;

  &:focus,
  &:hover {
    outline: none;
    background-color: #a4d2ff;
  }

  &:active {
    outline: none;
    background-color: #005fbf;
    color: #ffffff;
  }
}

.numpad-row {
  display: flex;
  flex-flow: row;
  justify-content: center;
  align-items: center;

  .numpad-button {
    border-width: 1px;
    height: 50px;
    font-size: 100%;
  }
}
</style>
