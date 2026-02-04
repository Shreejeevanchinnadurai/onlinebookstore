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

@WebServlet("/borrowBook")
public class BorrowBookServlet extends HttpServlet {

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

        int bookId = Integer.parseInt(request.getParameter("book_id"));
        String borrowerName = request.getParameter("borrower_name");

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps1 = con.prepareStatement(
                "UPDATE books SET quantity = quantity - 1 WHERE book_id=? AND quantity>0");
            ps1.setInt(1, bookId);
            int updated = ps1.executeUpdate();

            out.println("<html><head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<link rel='stylesheet' href='style.css'>");
            out.println("</head><body><div class='container'>");

            if (updated == 0) {
                out.println("<h2 class='error'>❌ Book not available</h2>");
                out.println("<a href='BorrowBook.html'>Try Again</a>");
            } else {
                PreparedStatement ps2 = con.prepareStatement(
                    "INSERT INTO borrowed_books(book_id, borrower_name, borrow_date, status) " +
                    "VALUES (?, ?, CURDATE(), 'BORROWED')");
                ps2.setInt(1, bookId);
                ps2.setString(2, borrowerName);
                ps2.executeUpdate();

                out.println("<h2 class='success'>📘 Book borrowed successfully</h2>");
                out.println("<a href='index.html'>Back to Home</a>");
            }

            out.println("</div></body></html>");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
