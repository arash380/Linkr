import moment from "moment";
import { toast } from "react-toastify";

export const DEFAULT_RETURN_VALUE = "---";
export const OBJECT_WILD_CARD_KEY = ""; // returns the whole object

export const accessObjectProperty = (object, key) => {
  if (key === OBJECT_WILD_CARD_KEY) return object;

  let result;
  if (typeof key === "object") {
    const convertedSource = key.map((src) =>
      src.split(".").reduce((o, i) => o[i], object)
    );
    return convertedSource.join(" ");
  }
  try {
    result = key.split(".").reduce((o, i) => o[i], object);
  } catch (error) {}
  if (typeof result === "boolean") return result;
  if (result !== 0 && !result) return DEFAULT_RETURN_VALUE;
  return result;
};

export const dndReorder = (list, startIndex, endIndex) => {
  const result = Array.from(list);
  const [removed] = result.splice(startIndex, 1);
  result.splice(endIndex, 0, removed);
  return result;
};

export const _convertDateForPicker = (d) => {
  return moment(d).format("yyyy-MM-DD");
};

export const _catchAPIError = (err) => {
  if (err.response && err.response.status === 400 && err.response.data) {
    toast.error(err.response.data);
  } else if (err.response.status === 409) {
    toast.error("Some values are not unique!");
  } else {
    toast.error("Something went wrong!");
  }

  throw new Error("Failed!");
};

export const _getWpPrefix = (wpID) => {
  if (!wpID) return;
  let prefix;

  [...wpID].some((c, index) => {
    const isZero = c === 0 || c === "0";

    if (isZero) {
      prefix = wpID.substring(0, index);
    }

    return isZero;
  });

  return prefix;
};

export const formatNumber = (num, decimalPoints = 3) => {
  const d = Math.pow(10, decimalPoints);
  return Math.round((num + Number.EPSILON) * d) / d;
};
