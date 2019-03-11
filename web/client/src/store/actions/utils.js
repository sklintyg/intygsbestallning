
const ROOT_URL = '/api/';

const handleResponse = (config) => (response) => {
  if (!response.ok) {
    var error = {message: response.statusText, response}
    throw error;
  }

  if (config) {
    if (config.noBody) {
      return {};
    }
  }

  return response.json();
}

export const makeServerRequest = (path, config={}) => {
    return fetch(`${ROOT_URL}${path}`).then(handleResponse(config));
};

export const makeServerPost = (path, body, config) => {
  const url = `${ROOT_URL}${path}`;

  return fetch(url, {
    method: "POST",
    headers: {
        "Content-Type": "application/json",
    },
    body: JSON.stringify(body)
  })
  .then(handleResponse(config));
};

export const makeServerFormPost = (path, body, config) => {
  const url = `${ROOT_URL}${path}`;

  return fetch(url, {
    method: "POST",
    headers: {
        "Content-Type": "application/x-www-form-urlencoded",
    },
    body: body
  })
  .then(handleResponse(config));
};

export const makeServerPut = (path, body, config) => {
  const url = `${ROOT_URL}${path}`;

  return fetch(url, {
    method: "PUT",
    headers: {
        "Content-Type": "application/json",
    },
    body: JSON.stringify(body)
  })
  .then(handleResponse(config));
};

export const makeServerDelete = (path, body, config) => {
  const url = `${ROOT_URL}${path}`;

  return fetch(url, {
    method: "DELETE",
    headers: {
        "Content-Type": "application/json",
    },
    body: JSON.stringify(body)
  })
  .then(handleResponse(config));
};
