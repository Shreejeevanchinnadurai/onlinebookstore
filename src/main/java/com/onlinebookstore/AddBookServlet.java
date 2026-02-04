package com.onlinebookstore;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/addBook")
public class AddBookServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 🔐 Login check
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("Login.html");
            return;
        }

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String title = request.getParameter("title");
        String author = request.getParameter("author");
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO books(title, author, quantity) VALUES (?, ?, ?)");
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setInt(3, quantity);

            int i = ps.executeUpdate();

            out.println("<html><head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<link rel='stylesheet' href='style.css'>");
            out.println("</head><body><div class='container'>");

            if (i > 0) {
                out.println("<h2 class='success'>✅ Book added successfully</h2>");
            } else {
                out.println("<h2 class='error'>❌ Failed to add book</h2>");
            }

            out.println("<a href='addBook.html'>Add Another</a><br>");
            out.println("<a href='index.html'>Back to Home</a>");
            out.println("</div></body></html>");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
