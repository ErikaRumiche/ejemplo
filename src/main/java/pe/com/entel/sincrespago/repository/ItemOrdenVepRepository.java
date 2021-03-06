package pe.com.entel.sincrespago.repository;

import oracle.jdbc.OracleTypes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.support.oracle.SqlReturnStructArray;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import pe.com.entel.sincrespago.domain.TmpSincResPago;
import pe.com.entel.sincrespago.exception.RepositoryException;
import pe.com.entel.sincrespago.mapper.ItemOrdenVepMapper;
import pe.com.entel.sincrespago.util.Configuration;
import pe.com.entel.sincrespago.domain.ItemOrdenVep;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Erika Rumiche on 21/09/2018.
 */
@Repository
public class ItemOrdenVepRepository{

    private Logger logger = Logger.getLogger(ItemOrdenVepRepository.class);

    @Autowired
    private DataSource dataSourcePias;

    @Autowired
    private Configuration configuration;

    public List<ItemOrdenVep> obtenerOrdenVep() throws RepositoryException {

        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSourcePias);
        jdbcCall.withSchemaName(configuration.getWebsalesSchema());
        jdbcCall.withCatalogName(configuration.getSincresppagoPackage());
        jdbcCall.withProcedureName(configuration.getRespOvepcerradaSp());

        jdbcCall.addDeclaredParameter(new SqlOutParameter("AT_ITEM_OVEP", Types.ARRAY, "WEBSALES.TT_ITEM_OVEP", new SqlReturnStructArray<ItemOrdenVep>(new ItemOrdenVepMapper())));
        jdbcCall.addDeclaredParameter(new SqlOutParameter("AVCH_MENSAJE", OracleTypes.VARCHAR));

        MapSqlParameterSource in = new MapSqlParameterSource();
        Map<String, Object> result = jdbcCall.execute(in);
        String message = (String) result.get("AVCH_MENSAJE");

        if(message != null){
            throw new RepositoryException(message);
        }

        Object[] objects= (Object[])result.get("AT_ITEM_OVEP");

        List<ItemOrdenVep> itemOrdenVepList = new ArrayList<ItemOrdenVep>();
        for(Object object : objects){
            ItemOrdenVep itemOrdenVep = (ItemOrdenVep)object;
            itemOrdenVepList.add(itemOrdenVep);
        }

        return itemOrdenVepList;
    }

    public void actualizarOrdenVep(long orderId, long siteId) throws RepositoryException {

        try {

            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSourcePias);
            jdbcCall.withSchemaName(configuration.getWebsalesSchema());
            jdbcCall.withCatalogName(configuration.getSincresppagoPackage());
            jdbcCall.withProcedureName(configuration.getActualizarovepSp());

            jdbcCall.addDeclaredParameter(new SqlParameter("ANUM_ORDEN", OracleTypes.NUMBER));
            jdbcCall.addDeclaredParameter(new SqlParameter("ANUM_SITE", OracleTypes.NUMBER));
            jdbcCall.addDeclaredParameter(new SqlOutParameter("AVCH_MENSAJE", OracleTypes.VARCHAR));

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("ANUM_ORDEN", orderId)
                    .addValue("ANUM_SITE", siteId);

            Map<String, Object> result = jdbcCall.execute(in);
            String message = (String) result.get("AVCH_MENSAJE");

            if (message != null) {
                throw new RepositoryException(message);
            }

        } catch (Exception e) {
            throw new RepositoryException("No se pudo actualizar el site de la orden vep", e);
        }
    }

    public void insertarTemporal(TmpSincResPago tmpSincResPago) throws RepositoryException {

        try {

            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSourcePias);
            jdbcCall.withSchemaName(configuration.getWebsalesSchema());
            jdbcCall.withCatalogName(configuration.getSincresppagoPackage());
            jdbcCall.withProcedureName(configuration.getInstmpSp());

            jdbcCall.addDeclaredParameter(new SqlParameter("ANUM_ORDEN", OracleTypes.NUMBER));
            jdbcCall.addDeclaredParameter(new SqlParameter("ANUM_ORDERSITEID", OracleTypes.NUMBER));
            jdbcCall.addDeclaredParameter(new SqlParameter("ANUM_ORDERCLIENTECRMID", OracleTypes.NUMBER));
            jdbcCall.addDeclaredParameter(new SqlParameter("ANUM_OVEPID", OracleTypes.NUMBER));
            jdbcCall.addDeclaredParameter(new SqlParameter("AVCH_CUSTCODE", OracleTypes.VARCHAR));
            jdbcCall.addDeclaredParameter(new SqlParameter("ANUM_SITEID", OracleTypes.NUMBER));
            jdbcCall.addDeclaredParameter(new SqlParameter("AVCH_SITEESTADO", OracleTypes.VARCHAR));
            jdbcCall.addDeclaredParameter(new SqlOutParameter("AVCH_MENSAJE", OracleTypes.VARCHAR));

            SqlParameterSource in = new MapSqlParameterSource()
                    .addValue("ANUM_ORDEN", tmpSincResPago.getOrderId())
                    .addValue("ANUM_ORDERSITEID", tmpSincResPago.getOrderSiteId())
                    .addValue("ANUM_ORDERCLIENTECRMID", tmpSincResPago.getClienteCrmId())
                    .addValue("ANUM_OVEPID", tmpSincResPago.getOvepId())
                    .addValue("AVCH_CUSTCODE", tmpSincResPago.getCustcode())
                    .addValue("ANUM_SITEID", tmpSincResPago.getSiteId())
                    .addValue("AVCH_SITEESTADO", tmpSincResPago.getSiteEstado());

            Map<String, Object> result = jdbcCall.execute(in);
            String message = (String) result.get("AVCH_MENSAJE");

            if (message != null) {
                throw new RepositoryException(message);
            }

        } catch (Exception e) {
            throw new RepositoryException("No se pudo insertar ordenes vep actualizadas", e);
        }
    }

    public void truncateTemporal() throws RepositoryException {

        try {

            SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSourcePias);
            jdbcCall.withSchemaName(configuration.getWebsalesSchema());
            jdbcCall.withCatalogName(configuration.getSincresppagoPackage());
            jdbcCall.withProcedureName(configuration.getTruncatetmpSp());

            jdbcCall.addDeclaredParameter(new SqlOutParameter("AVCH_MENSAJE", OracleTypes.VARCHAR));

            SqlParameterSource in = new MapSqlParameterSource();

            Map<String, Object> result = jdbcCall.execute(in);
            String message = (String) result.get("AVCH_MENSAJE");

            if (message != null) {
                throw new RepositoryException(message);
            }

        } catch (Exception e) {
            throw new RepositoryException("No se pudo realizar el truncate de la tabla temporal", e);
        }
    }

}
