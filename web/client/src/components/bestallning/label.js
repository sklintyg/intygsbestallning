const common = {'ok': 'Ok'}

const Label = (key, messages) => {
  if (messages) {
    return messages[key];
  }
  return common[key];
}

export default Label;