
const ROOT_URL = '/api/';

const handleResponse = (config) => (response) => {
  if (!response.ok) {
    return response.json().then(errorJson => {
      const error = {statusCode: response.status, error: errorJson};
      throw error;
    });
  }

  if (config) {
    if (config.emptyBody) {
      return {};
    }
  }

  return response.json();
}

export const buildUrlFromParams = (path, parameters) => {
  let parameterList = []
  if (parameters) {
    Object.keys(parameters).forEach((key) => {
      let value = parameters[key];

      if (value) {

        parameterList.push(`${key}=${value}`);
      }
    });
  }

  let urlParameters = parameterList.join('&');

  if (urlParameters) {
    urlParameters = '?' + urlParameters;
  }

  return path + urlParameters;
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
