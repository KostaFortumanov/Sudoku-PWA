import axios from 'axios';
import LeaderboardService from './leaderboard-service'

const API_URL = 'http://localhost:9091/api/auth/';

class AuthService {
  login(user: { username: string; password: string; }) {
    return axios
      .post(API_URL + 'login', {
        username: user.username,
        password: user.password
      })
      .then((response: { data: { token: any; }; }) => {
        if (response.data.token) {
          localStorage.setItem('user', JSON.stringify(response.data));
        }

        LeaderboardService.getMyTimes('easy');
        LeaderboardService.getMyTimes('medium');
        LeaderboardService.getMyTimes('hard');

        return response.data;
      });
  }

  logout() {
    localStorage.clear();
  }

  register(user: { username: string; password: string; }) {
    return axios.post(API_URL + 'register', {
      username: user.username,
      password: user.password
    });
  }
}

export default new AuthService();