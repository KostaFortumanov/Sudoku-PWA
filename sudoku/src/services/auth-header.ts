export default function authHeader() {
  const user = localStorage.getItem("user");

  if (user) {
    const token = JSON.parse(user).token;
    return { 'Authorization': `Bearer ${token}` };
  } else {
    return { 'Authorization': ''};
  }
}
