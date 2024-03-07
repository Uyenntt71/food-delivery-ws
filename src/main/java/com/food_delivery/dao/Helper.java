package com.food_delivery.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.sql.DataSource;

import io.vavr.CheckedConsumer;
import io.vavr.CheckedFunction1;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Helper {
    public static <A> Function<Throwable, A> errSupply(A value) {
        return err -> {
            log.error("Exception running sql query", err);
            return value;
        };
    }

    /**
     * Execute SQL command, apply for INSERT/UPDATE/DELETE
     *
     * @param source {@link DataSource}
     * @param sql SQL command
     * @param c A function which fills out the place holders in SQL command
     *
     * @return See {@link PreparedStatement#executeUpdate()}
     */
    static int execute(DataSource source, String sql, CheckedConsumer<PreparedStatement> c) {
        return tryExecutePrep(source, sql, c, PreparedStatement::executeUpdate)
                .getOrElseGet(errSupply(0));
    }

    static <T> Try<T> tryExecutePrep(DataSource source,
                                     String sql,
                                     CheckedConsumer<PreparedStatement> c,
                                     CheckedFunction1<PreparedStatement, T> executor) {
        return
                Try.withResources(source::getConnection)
                   .of(conn -> Try.withResources(() -> conn.prepareStatement(sql))
                                  .of(prepStm -> {
                                      c.accept(prepStm);
                                      return executor.apply(prepStm);
                                  })
                      ).flatMap(t -> t);
    }

    /**
     * @param source    DataSource
     * @param sql       SQL query to execute
     * @param rowMapper A function to extract from a {@link ResultSet}
     * @param c         A function which fills out the place holders in SQL command
     */
    static <T> List<T> select(DataSource source,
                              String sql,
                              Function<ResultSet, Optional<T>> rowMapper,
                              CheckedConsumer<PreparedStatement> c) {
        return Try.withResources(source::getConnection).of(conn ->
                                                                   Try.withResources(() -> conn.prepareStatement(sql)).of(prepStm -> {
                                                                       c.accept(prepStm);
                                                                       return extractRs(prepStm.executeQuery(), rowMapper);
                                                                   })
                                                          ).flatMap(t -> t).getOrElseGet(errSupply(Collections.emptyList()));
    }

    /**
     * Extract rows from a {@link ResultSet}. Caller of this fn is responsible for closing {@link ResultSet}
     */
    static <T> List<T> extractRs(ResultSet rs, Function<ResultSet, Optional<T>> rowMapper) {
        return Try.of(() -> {
            List<T> output = new ArrayList<>();
            while (rs.next()) {
                rowMapper.apply(rs).ifPresent(output::add);
            }
            return output;
        }).getOrElseGet(errSupply(Collections.emptyList()));
    }


}
