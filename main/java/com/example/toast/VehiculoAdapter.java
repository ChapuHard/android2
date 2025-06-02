package com.example.toast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adaptador para el RecyclerView que muestra la lista de vehículos
 * Maneja la visualización de datos y eventos de click
 */
public class VehiculoAdapter extends RecyclerView.Adapter<VehiculoAdapter.VehiculoViewHolder> {

    private List<Vehiculo> listaVehiculos;
    private OnVehiculoListener listener;

    /**
     * Interface para manejar eventos de click en los items
     */
    public interface OnVehiculoListener {
        void onVehiculoClick(int position);
        void onEditarClick(int position);
        void onEliminarClick(int position);
    }

    /**
     * Constructor del adaptador
     * @param listaVehiculos Lista de vehículos a mostrar
     * @param listener Listener para eventos de click
     */
    public VehiculoAdapter(List<Vehiculo> listaVehiculos, OnVehiculoListener listener) {
        this.listaVehiculos = listaVehiculos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VehiculoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vehiculo, parent, false);
        return new VehiculoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VehiculoViewHolder holder, int position) {
        Vehiculo vehiculo = listaVehiculos.get(position);
        holder.bind(vehiculo, position);
    }

    @Override
    public int getItemCount() {
        return listaVehiculos.size();
    }

    /**
     * ViewHolder para cada item de vehículo
     */
    public class VehiculoViewHolder extends RecyclerView.ViewHolder {

        private TextView tvPlaca, tvPropietario, tvTipoVehiculo, tvFechaHora;
        private ImageButton btnEditar, btnEliminar;

        public VehiculoViewHolder(@NonNull View itemView) {
            super(itemView);

            // Inicializar vistas
            tvPlaca = itemView.findViewById(R.id.tvPlaca);
            tvPropietario = itemView.findViewById(R.id.tvPropietario);
            tvTipoVehiculo = itemView.findViewById(R.id.tvTipoVehiculo);
            tvFechaHora = itemView.findViewById(R.id.tvFechaHora);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }

        /**
         * Método para enlazar datos del vehículo con las vistas
         * @param vehiculo Objeto vehículo con los datos
         * @param position Posición en la lista
         */
        public void bind(Vehiculo vehiculo, int position) {
            // Asignar datos a las vistas
            tvPlaca.setText(vehiculo.getPlaca());
            tvPropietario.setText(vehiculo.getPropietario());
            tvTipoVehiculo.setText(vehiculo.getTipoVehiculo());
            tvFechaHora.setText(vehiculo.getFechaIngreso() + " - " + vehiculo.getHoraIngreso());

            // Configurar evento click en el item completo
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onVehiculoClick(position);
                    }
                }
            });

            // Configurar evento click en botón editar
            btnEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onEditarClick(position);
                    }
                }
            });

            // Configurar evento click en botón eliminar
            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onEliminarClick(position);
                    }
                }
            });
        }
    }

    /**
     * Método para actualizar la lista de vehículos
     * @param nuevaLista Nueva lista de vehículos
     */
    public void actualizarLista(List<Vehiculo> nuevaLista) {
        this.listaVehiculos = nuevaLista;
        notifyDataSetChanged();
    }

    /**
     * Método para obtener un vehículo en una posición específica
     * @param position Posición del vehículo
     * @return Objeto Vehiculo
     */
    public Vehiculo getVehiculo(int position) {
        return listaVehiculos.get(position);
    }
}