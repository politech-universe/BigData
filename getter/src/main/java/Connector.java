import entities.Category;
import entities.Region;
import entities.User;
import entities.Video;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Connector {



    static List<Region> getRegions(Connection con) throws  SQLException {
        List<Region> result = new ArrayList<>();
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_GET_REGIONS);
        while (resultSet.next()) {
            result.add(new Region(
                    resultSet.getInt("id"),
                    resultSet.getString("abb"),
                    resultSet.getString("name"))
            );
        }
        statement.close();

        return result;
    }

    static String SQL_GET_REGIONS = "select * from regions";
    static final String SQL_INSERT_USER = "INSERT INTO channels VALUES (?, ?, ?)";
    static final String SQL_INSERT_CATEGORY = "INSERT INTO categories VALUES (%s, '%s')";
    static final String SQL_INSERT_VIDEO = "INSERT INTO videos VALUES (?,?,?,?,?,?,?,?,?)";
    static final String SQL_INSERT_VIDEO_REGION = "INSERT INTO video_region VALUES ('%s', %s,%s,'%s')";
    static final String SQL_CHECK_USER = "select * from channels where id='%s'";
    static final String SQL_CHECK_CATEGORY = "select * from categories where id='%s'";
    static final String SQL_CHECK_VIDEO = "select * from videos where id='%s'";
    static final String SQL_CHECK_VIDEO_REGION = "select * from video_region where videoId='%s' and regionid=%s";


    static boolean hasCategory(int c, Connection con) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery(String.format(SQL_CHECK_CATEGORY, c));

        return rs.next();
    }

    static boolean hasUser(String u, Connection con) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery(String.format(SQL_CHECK_USER, u));

        return rs.next();
    }

    static boolean hasVideo(String v, Connection con) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery(String.format(SQL_CHECK_VIDEO, v));

        return rs.next();
    }

    static boolean hasVideoRegion(String v, int r, Connection con) throws SQLException {
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery(String.format(SQL_CHECK_VIDEO_REGION, v, r));

        return rs.next();
    }

    static void pushUser(User u, Connection con) throws SQLException {

        PreparedStatement preparedStatement = con.prepareStatement(SQL_INSERT_USER);
        preparedStatement.setString(1, u.getId());
        preparedStatement.setString(2, u.getName());
        preparedStatement.setInt(3, u.getSubs());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }


    static void pushCategory(Category c, Connection con) throws SQLException {
        Statement statement = con.createStatement();
        statement.execute(String.format(SQL_INSERT_CATEGORY, c.getId(), c.getName()));
        statement.close();
    }

    static void pushVideo(Video v, Connection con) throws SQLException {

        PreparedStatement preparedStatement = con.prepareStatement(SQL_INSERT_VIDEO);
        preparedStatement.setString(1, v.getId());
        preparedStatement.setString(2, v.getName());
        preparedStatement.setString(3, v.getUserId());
        preparedStatement.setInt(4, v.getViews());
        preparedStatement.setInt(5, v.getCategory());
        preparedStatement.setInt(6, v.getDlikes());
        preparedStatement.setInt(7, v.getLikes());
        preparedStatement.setDate(8, Date.valueOf(v.getDate()));
        preparedStatement.setInt(9, v.getComments());
        preparedStatement.executeUpdate();
        preparedStatement.close();


    }

    static void pushVideoRegion(Video v, Connection con) throws SQLException {
        Statement statement = con.createStatement();


        statement.execute(String.format(SQL_INSERT_VIDEO_REGION,
                v.getId(),
                v.getRegionId(),
                v.getTrendNum(),
                v.getTrendDate()));

        statement.close();
    }


}
