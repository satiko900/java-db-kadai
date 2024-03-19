package kadai_007;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Posts_Chapter07 {
		
	//必要な変数を用意	
	int Rowstotal = 0;
    int[] idLists = {1003, 1002, 1003, 1001, 1002};
    String[] dateLists = { "2023-02-08", "2023-02-08", "2023-02-09", "2023-02-09", "2023-02-10"};
    String[] contentLists = { "昨日の夜は徹夜でした・・", "お疲れ様です！","今日も頑張ります！", "無理は禁物ですよ!", "明日から連休ですね！"};
    int[] likesLists = {13, 12, 18, 17, 20 };
    
    //フィールドを用意
    private Connection con = null;
    private PreparedStatement statement = null;
        
        
   // データベースに接続するメソッド       
    public void getConnection() {
    	try {
    		con = DriverManager.getConnection(
                "jdbc:mysql://localhost/challenge_java",
                "root",
                "password"
            );
            System.out.println("データベース接続成功:" + con);
    	} catch(SQLException e) {
    		System.out.println("データベース接続失敗：" + e.getMessage());
    	}
    }

    // SQLクエリを準備するメソッド
    public void createStatement() {                   
    	String sql = "INSERT INTO posts (user_id, posted_at, post_content, likes) VALUES (?, ?, ?, ?);";
    	try { 
    		statement = con.prepareStatement(sql);
    	} catch (SQLException e) {
    		System.out.println("ステートメントの作成に失敗しました：" + e.getMessage());
    	}
    }
      
    //投稿データを追加するメソッド    
    public void executeUpdate() {   
    	System.out.println("レコード追加を実行します");
  
        try {
        	for(int i = 0; i < idLists.length; i++) {
        		statement.setInt(1,idLists[i]);
        		statement.setString(2, dateLists[i]);
        		statement.setString(3, contentLists[i]);
        		statement.setInt(4, likesLists[i]);
            	
        		int rowCnt = statement.executeUpdate();
            	Rowstotal += rowCnt;
        } 
        	System.out.println(Rowstotal + "件のレコードが追加されました"); 
          }catch(SQLException e) {
            System.out.println("エラー発生：" + e.getMessage());
        } 
    }
         
       //投稿データを検索するメソッド   
        public void executeQuery() {
        	System.out.println("ユーザーIDが1002のレコードを検索しました");
        	
        	try {
        		String search = "SELECT * FROM posts WHERE user_id = 1002;";
        		ResultSet result = statement.executeQuery(search);
        		
        		while(result.next()) {
        		Date postedAt = result.getDate("posted_at");
        		String postContent = result.getString("post_content");
        		int likes = result.getInt("likes");
        		
        		System.out.println(result.getRow() + "件目：" + "投稿日時＝" + postedAt + "/投稿内容＝" + postContent + "/いいね数＝" + likes);
        		}
        	} catch(SQLException e) {
        		System.out.println("エラー発生" + e.getMessage());
        	} 
        }


	public static void main(String[] args) {
        		Posts_Chapter07 posts = new Posts_Chapter07();
        		posts.getConnection();
        		posts.createStatement();
        		posts.executeUpdate();
        		posts.executeQuery();
        		
        	try {
        		if (posts.statement != null) posts.statement.close();
        		if (posts.con != null) posts.con.close();
            } catch(SQLException e) {
            	System.out.println("データベース接続のクローズに失敗しました：" + e.getMessage());
        }
      }
    }

