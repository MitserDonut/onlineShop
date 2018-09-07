package cn.work.domain;

public class Admin {
		private String aid;
		private String adname;
		private String adpassword;
		
		@Override
		public String toString() {
			return "Admin [aid=" + aid + ", adname=" + adname + ", adpassword=" + adpassword + ", getAid()=" + getAid()
					+ ", getAdname()=" + getAdname() + ", getAdpassword()=" + getAdpassword() + ", getClass()="
					+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
		}
		public String getAid() {
			return aid;
		}
		public void setAid(String aid) {
			this.aid = aid;
		}
		public String getAdname() {
			return adname;
		}
		public void setAdname(String adname) {
			this.adname = adname;
		}
		public String getAdpassword() {
			return adpassword;
		}
		public void setAdpassword(String adpassword) {
			this.adpassword = adpassword;
		}
		
		
}
