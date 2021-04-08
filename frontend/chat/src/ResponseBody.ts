export interface ResponseBody<T> {
  code: number;
  message: string;
  reason: string;
  data: T;
  success: boolean;
}
