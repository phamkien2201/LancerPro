export interface IProjectDetail {
  id: string;
  clientId: string;
  title: string;
  budgetMax: number;
  budgetMin: number;
  description: string;
  skills?: string[];
  paymentMethod: string;
  address: string;
  category: string;
  email: string;
  workPattern: string;
  status: string;
  deadline: string;
  createdAt: string;
  updatedAt: string;
}
export interface ProjectDetail {
  id: string;
  clientId: string;
  title: string;
  budgetMax: number;
  budgetMin: number;
  description: string;
  skills?: string[];
  paymentMethod: string;
  address: string;
  category: string;
  email: string;
  workPattern: string;
  status: string;
  deadline: string;
  createdAt: string;
  updatedAt: string;
}
export interface IProjectResponse {
  products: IProjectDetail[];
  totalPages: number;
}

export interface IProject {
  code: number;
  message: string;
  result: IProjectResponse;
}
export interface IProjects {
  code: number;
  message: string;
  result: IProjectDetail;
}
