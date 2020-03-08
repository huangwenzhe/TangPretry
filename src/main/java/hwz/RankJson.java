package hwz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet ("/ranks.json")
public class RankJson  extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");

        String con = req.getParameter("con");
        if (con == null) {
            con = "5";
        }
        JSONArray js = new JSONArray();
        try (Connection connection = DBconfig.getConnection()) {
            String sql = " select count(*) AS cnt ,author from tangpoetry group by author HAVING CNT>= ? ORDER BY CNT DESC";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, con);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String author = resultSet.getString("author");
                        int count = resultSet.getInt("cnt");
                        JSONArray item = new JSONArray();
                        item.add(author);
                        item.add(count);
                        js.add(item);
                    }
                    resp.getWriter().println(js.toJSONString());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("error",e.getMessage());
            resp.getWriter().println(jsonObject.toJSONString());
//        }
//        JSONArray jsonArray = new JSONArray();
//        {
//            JSONArray iteam = new JSONArray();
//            iteam.add("李白");
//            iteam.add(10);
//            jsonArray.add(iteam);
//        }
//        {
//            JSONArray iteam = new JSONArray();
//            iteam.add("杜甫");
//            iteam.add(6);
//            jsonArray.add(iteam);
//        }
//        {
//            JSONArray iteam = new JSONArray();
//            iteam.add("王维");
//            iteam.add(2);
//            jsonArray.add(iteam);
//        }

            //resp.getWriter().println(jsonArray.toJSONString());
        }
    }
}
