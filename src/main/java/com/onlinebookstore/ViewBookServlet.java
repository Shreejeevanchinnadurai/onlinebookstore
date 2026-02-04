package com.onlinebookstore;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/viewBooks")
public class ViewBookServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔐 Login check
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("Login.html");
            return;
        }

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html><head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<link rel='stylesheet' href='style.css'>");
        out.println("</head><body>");

        out.println("<div class='table-card'>");
        out.println("<h2>Book List</h2>");

        out.println("<table>");
        out.println("<tr>");
        out.println("<th>ID</th>");
        out.println("<th>Title</th>");
        out.println("<th>Author</th>");
        out.println("<th>Quantity</th>");
        out.println("<th>Action</th>");
        out.println("</tr>");

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM books");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int bookId = rs.getInt("book_id");

                out.println("<tr>");
                out.println("<td>" + bookId + "</td>");
                out.println("<td>" + rs.getString("title") + "</td>");
                out.println("<td>" + rs.getString("author") + "</td>");
                out.println("<td>" + rs.getInt("quantity") + "</td>");

                // DELETE BUTTON
                out.println("<td>");
                out.println("<a href='deleteBook?book_id=" + bookId + "' ");
                out.println("onclick=\"return confirm('Are you sure you want to delete this book?');\">");
                out.println("Delete</a>");
                out.println("</td>");

                out.println("</tr>");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        out.println("</table>");
        out.println("<a class='link-btn' href='index.html'>Back to Home</a>");
        out.println("</div>");

        out.println("</body></html>");
    }
}
