export default function authHeader() {
  let user = localStorage.getItem("user");

  if (user) {
    let token = JSON.parse(user).token;
    return { Authorization: "Bearer " + token };
  } else {
    return {};
  }
}
