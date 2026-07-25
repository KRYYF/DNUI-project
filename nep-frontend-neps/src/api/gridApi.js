const baseUrl = "http://localhost:8080/grid";

export function gridLogin(data) {
  return fetch(`${baseUrl}/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(data)
  });
}

export function getTaskList(gmId, pageNum, pageSize) {
  return fetch(`${baseUrl}/task/list?gmId=${gmId}&pageNum=${pageNum}&pageSize=${pageSize}`);
}

export function getTaskDetail(afId) {
  return fetch(`${baseUrl}/task/${afId}`);
}

export function submitDetect(data) {
  return fetch(`${baseUrl}/submit`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(data)
  });
}
