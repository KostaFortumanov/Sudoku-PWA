import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://navigation-api.duckdns.org:9091/api/notifications/';
class NotificationService {

    subscribe(token: string) {
        return axios.get(API_URL + 'subscribe?token=' + token);
    }

    setToken(token: string) {
        return axios.get(API_URL + 'setToken?token=' + token, { headers: authHeader()});
    }
}

export default new NotificationService();