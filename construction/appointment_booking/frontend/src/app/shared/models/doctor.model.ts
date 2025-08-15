export interface DoctorProfile {
  uuid: string;
  firstName: string;
  lastName: string;
  professionalTitle: string;
  primarySpecialty: string;
  yearsExperience: number;
  biography: string;
  email: string;
  phone: string;
  address: {
    street: string;
    city: string;
    state: string;
    postalCode: string;
    country: string;
  };
  medicalLicenseNumber: string;
  certifications: string;
  memberships: string;
  avatar?: string;
}

export class DoctorProfile implements DoctorProfile {
  constructor(
    public uuid: string,
    public firstName: string,
    public lastName: string,
    public professionalTitle: string,
    public primarySpecialty: string,
    public yearsExperience: number,
    public biography: string,
    public email: string,
    public phone: string,
    public address: {
      street: string;
      city: string;
      state: string;
      postalCode: string;
      country: string;
    },
    public medicalLicenseNumber: string,
    public certifications: string,
    public memberships: string,
    public avatar?: string
  ) {}

  get fullName(): string {
    return `Dr. ${this.firstName} ${this.lastName}`;
  }

  static fromFormData(formData: any, uuid: string = ''): DoctorProfile {
    return new DoctorProfile(
      uuid,
      formData.firstName,
      formData.lastName,
      formData.professionalTitle,
      formData.primarySpecialty,
      formData.yearsExperience,
      formData.biography,
      formData.email,
      formData.phone,
      formData.address,
      formData.medicalLicenseNumber,
      formData.certifications,
      formData.memberships
    );
  }
}
