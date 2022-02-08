import axios from 'axios';
import LeaderboardService from './leaderboard-service';
import NotificationService from './notification-service';

const API_URL = 'http://navigation-api.duckdns.org:9091/api/auth/';

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

        const token = window.localStorage.getItem('notificationToken');
        if(token) {
          NotificationService.setToken(token);
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