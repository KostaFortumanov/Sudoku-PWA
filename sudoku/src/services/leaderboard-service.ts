import axios from "axios";
import authHeader from "./auth-header";

const API_URL = "http://localhost:9091/api/leaderboard/";

class LeaderboardService {
  getLeaderboard(difficulty: string) {
    return axios.get(API_URL + difficulty, { headers: authHeader() });
  }

  getMyTimes(difficulty: string) {
      return axios.get(API_URL + 'myTimes/' + difficulty, { headers: authHeader()}).then((response)=>{

          let old = window.localStorage.getItem(difficulty + "Time");

          let arr = [];
          for(const time of response.data) {
            arr.push({
                difficulty: difficulty,
                time: time,
                isBackedUp: true,
              });
          }
          window.localStorage.setItem(difficulty + "Time", JSON.stringify(arr));

          if(old) {
            arr = JSON.parse(old);
            for(let i=0; i<arr.length; i++) {
              if(!arr[i].isBackedUp) {
                this.saveTime(difficulty, arr[i].time)
              }
            }
          }
      })
  }

  saveTime(difficulty: string, time: number) {
    let isBackedUp: boolean;

    return axios
      .post(
        API_URL,
        {
          difficulty: difficulty,
          time: time,
        },
        { headers: authHeader() }
      )
      .then(
        () => {
          isBackedUp = true;
        }
      ).catch(() => {
          isBackedUp = false;
      })
      .then(() => {
        console.log('finnaly ' + time)
        const t = window.localStorage.getItem(difficulty + "Time");
        let arr = [];
        if (t) {
          arr = JSON.parse(t);
        }
        arr.push({
          difficulty: difficulty,
          time: time,
          isBackedUp: isBackedUp,
        });
        window.localStorage.setItem(difficulty + "Time", JSON.stringify(arr));
      });
  }
}

export default new LeaderboardService();
