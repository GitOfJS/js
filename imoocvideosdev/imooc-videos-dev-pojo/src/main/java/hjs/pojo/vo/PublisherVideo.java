package hjs.pojo.vo;

public class PublisherVideo {
    
	public UsersVO publisher;
	public boolean userLikeVideo;
	public UsersVO getPublisher() {
		return publisher;
	}
	public void setPublisher(UsersVO publisher) {
		this.publisher = publisher;
	}
	public boolean isUserLikeVideo() {
		return userLikeVideo;
	}
	public void setUserLikeVideo(boolean userLikeVideo) {
		this.userLikeVideo = userLikeVideo;
	}

	@Override
	public String toString() {
		return "PublisherVideo{" +
				"publisher=" + publisher +
				", userLikeVideo=" + userLikeVideo +
				'}';
	}
}